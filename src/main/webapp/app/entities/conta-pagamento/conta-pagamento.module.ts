import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContaPagamentoComponent } from './list/conta-pagamento.component';
import { ContaPagamentoDetailComponent } from './detail/conta-pagamento-detail.component';
import { ContaPagamentoUpdateComponent } from './update/conta-pagamento-update.component';
import { ContaPagamentoDeleteDialogComponent } from './delete/conta-pagamento-delete-dialog.component';
import { ContaPagamentoRoutingModule } from './route/conta-pagamento-routing.module';

@NgModule({
  imports: [SharedModule, ContaPagamentoRoutingModule],
  declarations: [
    ContaPagamentoComponent,
    ContaPagamentoDetailComponent,
    ContaPagamentoUpdateComponent,
    ContaPagamentoDeleteDialogComponent,
  ],
})
export class ContaPagamentoModule {}
