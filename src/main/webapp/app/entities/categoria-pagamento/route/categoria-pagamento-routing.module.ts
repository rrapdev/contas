import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaPagamentoComponent } from '../list/categoria-pagamento.component';
import { CategoriaPagamentoDetailComponent } from '../detail/categoria-pagamento-detail.component';
import { CategoriaPagamentoUpdateComponent } from '../update/categoria-pagamento-update.component';
import { CategoriaPagamentoRoutingResolveService } from './categoria-pagamento-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const categoriaPagamentoRoute: Routes = [
  {
    path: '',
    component: CategoriaPagamentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaPagamentoDetailComponent,
    resolve: {
      categoriaPagamento: CategoriaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaPagamentoUpdateComponent,
    resolve: {
      categoriaPagamento: CategoriaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaPagamentoUpdateComponent,
    resolve: {
      categoriaPagamento: CategoriaPagamentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaPagamentoRoute)],
  exports: [RouterModule],
})
export class CategoriaPagamentoRoutingModule {}
