import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PagadorComponent } from '../list/pagador.component';
import { PagadorDetailComponent } from '../detail/pagador-detail.component';
import { PagadorUpdateComponent } from '../update/pagador-update.component';
import { PagadorRoutingResolveService } from './pagador-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pagadorRoute: Routes = [
  {
    path: '',
    component: PagadorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PagadorDetailComponent,
    resolve: {
      pagador: PagadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PagadorUpdateComponent,
    resolve: {
      pagador: PagadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PagadorUpdateComponent,
    resolve: {
      pagador: PagadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pagadorRoute)],
  exports: [RouterModule],
})
export class PagadorRoutingModule {}
