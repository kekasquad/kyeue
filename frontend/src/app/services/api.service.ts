import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  cleanData(data: any): object {
    let res = {};
    if (data instanceof Array) {
      res = data.map(v => {
        if (v instanceof Object) {
          return this.cleanData(v);
        }
        return v;
      });
    } else {
      Object.keys(data).map((key) => {
        let v = data[key];
        if (typeof data[key] === 'string') {
          v = v.trim();
        }
        if (v instanceof Object) {
          const cl = this.cleanData(v);
          if (Object.keys(cl).length > 0) {
            res[key] = cl;
          }
        } else if (v !== null && v !== '') {
          res[key] = v;
        }
      });
    }
    return res;
  }

  signUp(data: object): Observable<User> {
    return this.http.post(`${environment.apiUrl}/auth/signup`, this.cleanData(data)).pipe(
      map(res => new User(res))
    );
  }
}
