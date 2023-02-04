import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaPagamento } from '../categoria-pagamento.model';
import { CategoriaPagamentoService } from '../service/categoria-pagamento.service';

@Injectable({ providedIn: 'root' })
export class CategoriaPagamentoRoutingResolveService implements Resolve<ICategoriaPagamento | null> {
  constructor(protected service: CategoriaPagamentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaPagamento | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaPagamento: HttpResponse<ICategoriaPagamento>) => {
          if (categoriaPagamento.body) {
            return of(categoriaPagamento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
