import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  register(user: any):Observable<any> {

    return this.http.post(`${this.apiUrl}/users/register`,user);
  }

  login(credentials: any): Observable<any> {

    return this.http.post(`${this.apiUrl}/users/login`,credentials);
  }

  getToken(): string | null {

    return localStorage.getItem('token');
  }

  saveToken(token: string): void {

    localStorage.setItem('token', token);
  }

  isAuthenticated():boolean {
    return !!this.getToken();
  }

  clearToken():void{
    localStorage.removeItem('token');
  }
}
