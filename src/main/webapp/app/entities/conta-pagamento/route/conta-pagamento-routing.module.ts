import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContaPagamentoComponent } from '../list/conta-pagamento.component';
import { ContaPagamentoDetailComponent } from '../detail/conta-pagamento-detail.component';
import { ContaPagamentoUpdateComponent } from '../update/conta-pagamento-update.component';
import { ContaPagamentoRoutingResolveService } from './conta-pagamento-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contaPagamentoRoute: Routes = [
  {
    path: '',
    component: ContaPagamentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContaPagamentoDetailComponent,
    resolve: {
      contaPagamento: ContaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContaPagamentoUpdateComponent,
    resolve: {
      contaPagamento: ContaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContaPagamentoUpdateComponent,
    resolve: {
      contaPagamento: ContaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contaPagamentoRoute)],
  exports: [RouterModule],
})
export class ContaPagamentoRoutingModule {}
