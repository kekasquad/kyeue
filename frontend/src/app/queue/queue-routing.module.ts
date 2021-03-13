import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { QueueListComponent } from './queue-list/queue-list.component';
import { AuthGuard } from '../helpers/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: QueueListComponent,
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QueueRoutingModule { }
