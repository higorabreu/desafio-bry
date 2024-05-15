import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListComponent } from './components/list/list.component';
import { RegisterComponent } from './components/register/register.component';
import { ShowComponent } from './components/show/show.component';
import { UpdateComponent } from './components/update/update.component';

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
