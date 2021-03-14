import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { FormControl, FormGroup } from '@angular/forms';
import { ServerErrorsInterface } from '../models/interfaces';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  loginForm = new FormGroup({
    username: new FormControl(),
    password: new FormControl(),
  });

  signUpForm = new FormGroup({
    username: new FormControl(),
    password: new FormControl(),
    firstName: new FormControl(),
    lastName: new FormControl()
  });

  isProcess = false;
  signUpMode = false;
  serverErrors: ServerErrorsInterface;

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {}

  changeMode(): void {
    if (this.isProcess) {
      return;
    }
    this.isProcess = true;
    this.signUpMode = !this.signUpMode;
    this.loginForm.reset();
    this.signUpForm.reset();
    this.serverErrors = null;
    this.isProcess = false;
  }

  logIn(): void {
    if (this.isProcess) {
      return;
    }

    this.isProcess = true;
    this.serverErrors = null;
    this.auth.login(
      this.loginForm.controls.username.value,
      this.loginForm.controls.username.value
    ).subscribe(
      () => {
        this.isProcess = false;
        this.router.navigate(['/']);
      },
      (err) => {
        this.serverErrors = err.error;
        this.isProcess = false;
      }
    );
  }

  signUp(): void {
    if (this.isProcess) {
      return;
    }
    this.isProcess = true;
    this.serverErrors = null;
    this.api.signUp(this.signUpForm.value).subscribe(
      () => {
        this.isProcess = false;
        this.changeMode();
      },
      (err) => {
        this.serverErrors = err.error;
        this.isProcess = false;
      }
    );
  }
}
