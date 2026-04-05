import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-signin',
  imports: [ReactiveFormsModule],
  templateUrl: './signin.html',
  styleUrl: './signin.css',
})
export class Signin {
  private backendService = inject(BackendService);
  private router = inject(Router);

  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  onLogin() {
    if (this.loginForm.valid) {
      this.backendService
          .postLogin(
              this.loginForm.get('username')?.value,
              this.loginForm.get('password')?.value)
          .subscribe(() => {this.router.navigate(['/'])});
    }
  }
}
