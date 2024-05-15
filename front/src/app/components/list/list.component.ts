import { environment } from '../../../environments/environment';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
})
export class ListComponent {

  users: User[] = []

  constructor(private userService : UserService) {
    this.listUsers()
    console.log(environment.api)
  }

  listUsers(){
    this.userService.getUsers()
    .subscribe(users => this.users = users)
  }

}
