import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup; // Reactive form instance
  isSubmitting = false; // State to track form submission

  constructor(private fb: FormBuilder, private http: HttpClient) {
    // Initialize the form with validators
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Getter for form controls
  get f() {
    return this.registerForm.controls;
  }

  // Register user
  register() {
    // Check if the form is invalid
    if (this.registerForm.invalid) {
      alert('Please fill out the form correctly!');
      return;
    }

    this.isSubmitting = true; // Disable button while submitting

    // HTTP request to register the user
    this.http.post('http://localhost:8080/api/users/register', this.registerForm.value)
      .subscribe({
        next: (response) => {
          console.log('Registration response:', response);
          alert('Registration successful!');
          this.registerForm.reset(); // Clear form after successful registration
        },
        error: (error) => {
          console.error('Error during registration:', error);

          // Enhanced error feedback for user
          if (error.status === 400) {
            alert('Bad Request: Please check the entered data.');
          } else if (error.status === 500) {
            alert('Server Error: Please try again later.');
          } else {
            alert('An unexpected error occurred. Please try again.');
          }
        },
        complete: () => {
          this.isSubmitting = false; // Re-enable the button after completion
        }
      });
  }
}