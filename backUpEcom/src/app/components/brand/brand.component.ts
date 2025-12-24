import { Component, OnInit } from '@angular/core';
import { Brand } from 'src/app/models/models';
import { BrandService } from 'src/app/services/brand.service';

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.scss']
})
export class BrandComponent implements OnInit {

  brands: Brand[] = [];
  brandForm: Brand = { name: '', description: '' };
  selectedFile?: File;
  editingId?: number;

  constructor(private brandService: BrandService) {}

  ngOnInit(): void {
    this.loadBrands();
  }

  loadBrands(): void {
    this.brandService.getAll().subscribe(data => {
      this.brands = data;
    });
  }

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  saveBrand(): void {
    if (this.editingId) {
      this.brandService
        .update(this.editingId, this.brandForm, this.selectedFile)
        .subscribe(() => {
          this.resetForm();
          this.loadBrands();
        });
    } else {
      this.brandService
        .create(this.brandForm, this.selectedFile)
        .subscribe(() => {
          this.resetForm();
          this.loadBrands();
        });
    }
  }

  editBrand(brand: Brand): void {
    this.brandForm = { 
      name: brand.name, 
      description: brand.description };
    this.editingId = brand.id;
    this.selectedFile = undefined;
  }

  deleteBrand(id?: number): void {
    if (!id) return;

    if (confirm('Are you sure?')) {
      this.brandService.delete(id).subscribe(() => {
        this.loadBrands();
      });
    }
  }

  resetForm(): void {
    this.brandForm = { name: '', description: '' };
    this.selectedFile = undefined;
    this.editingId = undefined;
  }
}
