import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICategoriaPagamento } from '../categoria-pagamento.model';
import { CategoriaPagamentoService } from '../service/categoria-pagamento.service';

import { CategoriaPagamentoRoutingResolveService } from './categoria-pagamento-routing-resolve.service';

describe('CategoriaPagamento routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoriaPagamentoRoutingResolveService;
  let service: CategoriaPagamentoService;
  let resultCategoriaPagamento: ICategoriaPagamento | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CategoriaPagamentoRoutingResolveService);
    service = TestBed.inject(CategoriaPagamentoService);
    resultCategoriaPagamento = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoriaPagamento returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaPagamento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaPagamento).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaPagamento = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoriaPagamento).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICategoriaPagamento>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaPagamento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaPagamento).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
