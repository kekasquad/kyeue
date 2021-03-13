import { Component, OnInit } from '@angular/core';
import {ApiService} from '../services/api.service';
import {FormControl, FormGroup} from '@angular/forms';

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

  signUpMode = false;

  constructor(api: ApiService) { }

  ngOnInit(): void {}

  changeMode(): void {
    this.signUpMode = !this.signUpMode;
    this.loginForm.reset();
    this.signUpForm.reset();
  }
}
