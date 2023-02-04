import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContaPagamento } from '../conta-pagamento.model';
import { ContaPagamentoService } from '../service/conta-pagamento.service';

@Injectable({ providedIn: 'root' })
export class ContaPagamentoRoutingResolveService implements Resolve<IContaPagamento | null> {
  constructor(protected service: ContaPagamentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContaPagamento | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contaPagamento: HttpResponse<IContaPagamento>) => {
          if (contaPagamento.body) {
            return of(contaPagamento.body);
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
