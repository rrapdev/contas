import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContaRecebimentoComponent } from './list/conta-recebimento.component';
import { ContaRecebimentoDetailComponent } from './detail/conta-recebimento-detail.component';
import { ContaRecebimentoUpdateComponent } from './update/conta-recebimento-update.component';
import { ContaRecebimentoDeleteDialogComponent } from './delete/conta-recebimento-delete-dialog.component';
import { ContaRecebimentoRoutingModule } from './route/conta-recebimento-routing.module';

@NgModule({
  imports: [SharedModule, ContaRecebimentoRoutingModule],
  declarations: [
    ContaRecebimentoComponent,
    ContaRecebimentoDetailComponent,
    ContaRecebimentoUpdateComponent,
    ContaRecebimentoDeleteDialogComponent,
  ],
})
export class ContaRecebimentoModule {}
