import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(private http:HttpClient, private router: Router, private authService:AuthService) {}

  login(){

    const credentials = {

      email: this.email,
      password: this.password
    };

        this.authService.login(credentials).subscribe({
          next: (response:any) => {

            if(response.token){
              this.authService.saveToken(response.token);
               alert('Login successful');
               this.router.navigate(['/dashboard']);
            }
                 
      },
      error: (err)=> {
        console.error(err);
        alert('Invalid credentials');
      },
    });
  }

}
