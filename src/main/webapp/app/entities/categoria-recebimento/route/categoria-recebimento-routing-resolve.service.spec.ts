import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICategoriaRecebimento } from '../categoria-recebimento.model';
import { CategoriaRecebimentoService } from '../service/categoria-recebimento.service';

import { CategoriaRecebimentoRoutingResolveService } from './categoria-recebimento-routing-resolve.service';

describe('CategoriaRecebimento routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoriaRecebimentoRoutingResolveService;
  let service: CategoriaRecebimentoService;
  let resultCategoriaRecebimento: ICategoriaRecebimento | null | undefined;

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
    routingResolveService = TestBed.inject(CategoriaRecebimentoRoutingResolveService);
    service = TestBed.inject(CategoriaRecebimentoService);
    resultCategoriaRecebimento = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoriaRecebimento returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaRecebimento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaRecebimento).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaRecebimento = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoriaRecebimento).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICategoriaRecebimento>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaRecebimento = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaRecebimento).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
