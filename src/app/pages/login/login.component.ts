import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';

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
  passwordVisible: boolean

  onPasswordVisibilityClick() {
    this.passwordVisible = !this.passwordVisible;
  }

  get passwordVisibilityIcon(): string {
    if (this.passwordVisible) {
      return 'eye-slash'
    }
    return 'eye'
  }

  get passwordInputType(): string {
    if (this.passwordVisible) {
      return 'text'
    }
    return 'password'
  }
}