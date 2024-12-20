import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  files: any[] = [];
  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    this.loadFiles();
  }

  // Load all files for the user
  loadFiles() {

    const headers = {
      Authorization: `Bearer ${localStorage.getItem('token')}`
    }
    this.http.get<any[]>('http://localhost:8080/api/files/list',{headers}).subscribe({
      next: (data) => {
        this.files = data;
        console.log('Files loaded:', this.files);
      },
      error: (err) => {
        console.error(err);
        alert('Failed to load files!');
      }
    });
  }

  deleteFile(files:any){
    console.log("delete");
  }

  downloadFile(fileId: number){
    this.http.get(`http://localhost:8080/api/files/download/${fileId}`,{
      responseType: 'blob',
    })
    .subscribe({
      next: (response) => {
        const url = window.URL.createObjectURL(response);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'filename';
        a.click();
      },
      error: (err)=> {
        console.log(err);
        alert('Error downloading file');
      },
    });
  }

}
