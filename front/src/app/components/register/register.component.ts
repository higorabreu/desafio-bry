import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  constructor(private userService: UserService) {}

  base64: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  onInputChanged(event: any) {
    let targetEvent = event.target;
    let file: File = targetEvent.files[0];
    let fileReader: FileReader = new FileReader();

    fileReader.onload = (e) => {
      let base64String: string = fileReader.result as string;
      let base64Content: string = base64String.split(',')[1];
      this.base64 = base64Content;
    };

    fileReader.readAsDataURL(file);
  }

  newUser = {
    name: '',
    cpf: '',
    picture: '',
  };

  createUser() {
    this.newUser.picture = this.base64;
    this.userService.createUser(this.newUser)
      .subscribe(
        () => {
          this.newUser = { name: '', cpf: '', picture: '' };
          this.errorMessage = '';
          this.successMessage = 'User created successfully.';
        },
        (error: HttpErrorResponse) => {
          if (error.status === 400) {
            this.successMessage = '';
            this.errorMessage = 'Invalid CPF.';
          } else if (error.status === 409) {
            this.successMessage = '';
            this.errorMessage = 'User already exists.';
          } else {
            this.successMessage = '';
            this.errorMessage = 'An unexpected error occurred.';
          }
        }
      );
  }
}
