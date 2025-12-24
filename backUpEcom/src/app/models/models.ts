
export interface User {
  userName: string;
  userFirstName?: string;
  userLastName?: string;
  email?: string;
  roles: string[];
}

export interface LoginResponse {
  jwtToken: string;
  user: {
    userName: string;
    userFirstName?: string;
    userLastName?: string;
    email?: string;
    roles: { roleName: string }[];
  };
}

export interface Brand {
  id?: number;
  name: string;
  description?: string;
  logoUrl?: string;
}
