import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html'
})
export class UpdateComponent {
  user: User = { name: '', cpf: '', picture: '' };
  userId: string = '';
  base64: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private userService: UserService,
  ) {}

  onInputChanged(event: any) {
    const file: File = event.target.files[0];
    const fileReader: FileReader = new FileReader();

    fileReader.onload = () => {
      const base64String: string = fileReader.result as string;
      const base64Content: string = base64String.split(',')[1];
      this.base64 = base64Content;
    };

    fileReader.readAsDataURL(file);
  }

  onIdChanged(userId: string) {
    if (userId) {
      this.getUserById(userId);
    } else {
      this.user.name = '';
      this.errorMessage = '';
    }
  }

  getUserById(userId: string) {
    this.userService.getUserById(userId).subscribe(
      user => {
        this.user = user;
        this.errorMessage = '';
      },
      error => {
        this.user.name = '';
        this.errorMessage = 'User not found.';
      }
    );
  }

  updateUser(): void {
    if (!this.user.id || !this.user.name) {
      return;
    }

    if (this.base64) {
      this.user.picture = this.base64;
    }

    this.userService.updateUser(this.user).subscribe(
      () => {
        this.errorMessage = '';
        this.successMessage = 'User updated successfully.';
      },
      (error) => {
        this.successMessage = '';
        this.errorMessage = 'Failed to update user.';
      }
    );
  }
}
