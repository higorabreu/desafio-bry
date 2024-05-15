import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private url = environment.api;

  constructor(private httpClient : HttpClient) {
  }

  getUsers(){
    return this.httpClient.get<User[]>(this.url + '/users')
  }

  getUserById(userId: string){
    return this.httpClient.get<User>(`${this.url}/user/${userId}`)
  }

  createUser(user: User): Observable<User> {
    return this.httpClient.post<User>(this.url + '/user', user);
  }

  deleteUser(userId: string) {
    const url = `${this.url}/user/${userId}`;
    return this.httpClient.delete(url);
  }

  updateUser(user: User): Observable<User> {
    return this.httpClient.put<User>(`${this.url}/user`, user);
  }


}
