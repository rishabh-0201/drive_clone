import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  selectedFile: File | null = null;

  constructor(private http: HttpClient, private authService: AuthService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      const headers = new HttpHeaders({
        Authorization: `Bearer ${this.authService.getToken()}`
      });

      this.http
        .post(`${environment.apiUrl}/files/upload`, formData, { headers })
        .subscribe({
          next: () => {
            alert('File uploaded successfully!');
          },
          error: (err) => {
            console.error(err);
            alert('File upload failed!');
          }
        });
    } else {
      alert('Please select a file first.');
    }
  }
}