import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoriaPagamentoComponent } from './list/categoria-pagamento.component';
import { CategoriaPagamentoDetailComponent } from './detail/categoria-pagamento-detail.component';
import { CategoriaPagamentoUpdateComponent } from './update/categoria-pagamento-update.component';
import { CategoriaPagamentoDeleteDialogComponent } from './delete/categoria-pagamento-delete-dialog.component';
import { CategoriaPagamentoRoutingModule } from './route/categoria-pagamento-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaPagamentoRoutingModule],
  declarations: [
    CategoriaPagamentoComponent,
    CategoriaPagamentoDetailComponent,
    CategoriaPagamentoUpdateComponent,
    CategoriaPagamentoDeleteDialogComponent,
  ],
})
export class CategoriaPagamentoModule {}
