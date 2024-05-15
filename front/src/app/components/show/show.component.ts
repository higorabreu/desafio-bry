import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { environment } from '../../../environments/environment';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
})
export class ShowComponent {

  users: User[] = []

  constructor(private userService : UserService, private sanitizer: DomSanitizer) {
    this.showUsers()
    console.log(environment.api)
  }

  showUsers(){
    this.userService.getUsers()
    .subscribe(users => this.users = users)
  }

  getUserImage(picture: string): SafeUrl {
    const imageUrl = 'data:image/jpeg;base64,' + picture;
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  }

  deleteUser(userId: string) {
    this.userService.deleteUser(userId)
      .subscribe(() => {
        this.showUsers();
      });
  }

}
