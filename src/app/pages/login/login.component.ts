import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';

const passwordMinLength: number = 8;

const passwordVisibleProps: PasswordFieldProps = {
  type: 'text',
  icon: 'eye-slash',
}

const passwordNotVisibleProps: PasswordFieldProps = {
  type: 'password',
  icon: 'eye',
}

type PasswordFieldProps = {
  type: string
  icon: string
}

enum LoginFormStatus {
  BASE = 'base',
  LOGGING_USER = 'logging-user',
  ERROR = 'error',
  SUCCESS = 'success'
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    InputTextModule,
    FormsModule,
    ReactiveFormsModule,
    FloatLabelModule,
    ButtonModule,
    InputGroupModule,
    InputGroupAddonModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  passwordFieldProps: PasswordFieldProps = passwordNotVisibleProps

  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(passwordMinLength)]],
    });
  }

  onPasswordVisibilityClick() {
    if (this.passwordFieldProps === passwordVisibleProps) {
      this.passwordFieldProps = passwordNotVisibleProps;
    } else {
      this.passwordFieldProps = passwordVisibleProps
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      console.log('Formulario enviado', this.loginForm.value);
    } else {
      this.loginForm.markAllAsTouched();
    }
  }

  get passwordInputType(): string {
    return this.passwordFieldProps.type
  }

  get passwordVisibilityIcon(): string {
    return this.passwordFieldProps.icon
  }

  get emailError(): string {
    const emailControl: FormControl = this.loginForm.get('email') as FormControl;

    if (emailControl.valid) {
      return '';
    }

    if (!(emailControl.touched || emailControl.dirty)) {
      return '';
    }

    if (emailControl.errors?.['required']) {
      return 'Email es requerido';
    }

    if (emailControl.errors?.['email']) {
      return 'Email inválido';
    }

    return '';
  }

  get passwordError(): string {
    const passwordControl: FormControl = this.loginForm.get('password') as FormControl;

    if (passwordControl.valid) {
      return '';
    }

    if (!(passwordControl.touched || passwordControl.dirty)) {
      return '';
    }

    if (passwordControl.errors?.['required']) {
      return 'Contraseña es requerida';
    }

    if (passwordControl.errors?.['minlength']) {
      return `Mínimo ${passwordMinLength} caracteres`;
    }

    return '';
  }

}