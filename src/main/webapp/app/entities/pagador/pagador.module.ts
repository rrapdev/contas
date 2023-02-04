import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PagadorComponent } from './list/pagador.component';
import { PagadorDetailComponent } from './detail/pagador-detail.component';
import { PagadorUpdateComponent } from './update/pagador-update.component';
import { PagadorDeleteDialogComponent } from './delete/pagador-delete-dialog.component';
import { PagadorRoutingModule } from './route/pagador-routing.module';

@NgModule({
  imports: [SharedModule, PagadorRoutingModule],
  declarations: [PagadorComponent, PagadorDetailComponent, PagadorUpdateComponent, PagadorDeleteDialogComponent],
})
export class PagadorModule {}
