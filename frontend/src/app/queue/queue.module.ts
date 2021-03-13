import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QueueListComponent } from './queue-list/queue-list.component';
import {QueueRoutingModule} from './queue-routing.module';



@NgModule({
  declarations: [
    QueueListComponent
  ],
  imports: [
    QueueRoutingModule,
    CommonModule,
  ]
})
export class QueueModule { }
