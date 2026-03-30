import {Component, inject, signal} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private backendService = inject(BackendService);

  protected registerFinish = signal(false);

  registerForm: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl(
        '',
        [
          Validators.required,
          Validators.minLength(8),
          this.checkContainRegex(/[A-Z]/, 'Uppercase'),
          this.checkContainRegex(/[a-z]/, 'Lowercase'),
          this.checkContainRegex(/\d/, 'Number'),
          this.checkContainRegex(/[!@#$%^&*(),.?":{}|<>]/, 'Special Char'),
        ]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  onRegister() {
    this.registerFinish.set(false);

    if (this.registerForm.valid) {
      this.backendService.postRegister(
          this.registerForm.get('username')?.value,
          this.registerForm.get('password')?.value,
          this.registerForm.get('email')?.value, () => {
            this.registerFinish.set(true);
          })
    }
  }

  private checkContainRegex(reg: RegExp, name: string) {
    return (control: AbstractControl) => {
      const value = control.value;
      if (value && !reg.test(value)) {
        let obj: {[id: string]: boolean} = {};
        obj[name] = true;

        return obj;
      }

      return null;
    }
  }
}
