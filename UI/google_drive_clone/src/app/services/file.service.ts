import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  uploadFile(file:File):Observable<any>{

    const formData = new FormData();
    formData.append('file',file);

    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    });

    return this.http.post(`${this.apiUrl}/files/upload`,formData,{headers});
  }
}
