export interface AuthResponseDto {
  token: string;
  message?: string;
}

export interface AuthRequestDto {
  username: string;
  password: string;
}
export type Role = 'ROLE_USER' | 'ROLE_ADMIN';

export interface UserResponse {
  id: number;
  username: string;
  role?: Role;
}

export interface WishlistItemResponseDto {
  id: number;
  user: UserResponse;
  destination: DestinationDto;
}

export interface DestinationDto {
  id?: number;
  country: string;
  capital: string;
  region: string;
  population: number;
  currency: string;
  flagImageUrl: string;
}

export interface SuggestionsResponse {
  suggestions: any[];
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
