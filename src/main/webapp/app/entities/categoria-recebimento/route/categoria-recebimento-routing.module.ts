import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaRecebimentoComponent } from '../list/categoria-recebimento.component';
import { CategoriaRecebimentoDetailComponent } from '../detail/categoria-recebimento-detail.component';
import { CategoriaRecebimentoUpdateComponent } from '../update/categoria-recebimento-update.component';
import { CategoriaRecebimentoRoutingResolveService } from './categoria-recebimento-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const categoriaRecebimentoRoute: Routes = [
  {
    path: '',
    component: CategoriaRecebimentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaRecebimentoDetailComponent,
    resolve: {
      categoriaRecebimento: CategoriaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaRecebimentoUpdateComponent,
    resolve: {
      categoriaRecebimento: CategoriaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaRecebimentoUpdateComponent,
    resolve: {
      categoriaRecebimento: CategoriaRecebimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaRecebimentoRoute)],
  exports: [RouterModule],
})
export class CategoriaRecebimentoRoutingModule {}
