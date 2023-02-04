import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoriaRecebimentoComponent } from './list/categoria-recebimento.component';
import { CategoriaRecebimentoDetailComponent } from './detail/categoria-recebimento-detail.component';
import { CategoriaRecebimentoUpdateComponent } from './update/categoria-recebimento-update.component';
import { CategoriaRecebimentoDeleteDialogComponent } from './delete/categoria-recebimento-delete-dialog.component';
import { CategoriaRecebimentoRoutingModule } from './route/categoria-recebimento-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaRecebimentoRoutingModule],
  declarations: [
    CategoriaRecebimentoComponent,
    CategoriaRecebimentoDetailComponent,
    CategoriaRecebimentoUpdateComponent,
    CategoriaRecebimentoDeleteDialogComponent,
  ],
})
export class CategoriaRecebimentoModule {}
