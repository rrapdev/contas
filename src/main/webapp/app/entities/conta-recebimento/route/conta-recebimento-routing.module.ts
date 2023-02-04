import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContaRecebimentoComponent } from '../list/conta-recebimento.component';
import { ContaRecebimentoDetailComponent } from '../detail/conta-recebimento-detail.component';
import { ContaRecebimentoUpdateComponent } from '../update/conta-recebimento-update.component';
import { ContaRecebimentoRoutingResolveService } from './conta-recebimento-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const contaRecebimentoRoute: Routes = [
  {
    path: '',
    component: ContaRecebimentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContaRecebimentoDetailComponent,
    resolve: {
      contaRecebimento: ContaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContaRecebimentoUpdateComponent,
    resolve: {
      contaRecebimento: ContaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContaRecebimentoUpdateComponent,
    resolve: {
      contaRecebimento: ContaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contaRecebimentoRoute)],
  exports: [RouterModule],
})
export class ContaRecebimentoRoutingModule {}
