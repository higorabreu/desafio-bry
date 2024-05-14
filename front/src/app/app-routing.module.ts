import { ListComponent } from './list/list.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { ShowComponent } from './show/show.component';
import { UpdateComponent } from './update/update.component';

const routes: Routes = [
  { path: 'list', component: ListComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'show', component: ShowComponent },
  { path: 'update', component: UpdateComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
