import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContaRecebimentoFormService } from './conta-recebimento-form.service';
import { ContaRecebimentoService } from '../service/conta-recebimento.service';
import { IContaRecebimento } from '../conta-recebimento.model';
import { IPagador } from 'app/entities/pagador/pagador.model';
import { PagadorService } from 'app/entities/pagador/service/pagador.service';
import { ICategoriaRecebimento } from 'app/entities/categoria-recebimento/categoria-recebimento.model';
import { CategoriaRecebimentoService } from 'app/entities/categoria-recebimento/service/categoria-recebimento.service';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { CaixaService } from 'app/entities/caixa/service/caixa.service';

import { ContaRecebimentoUpdateComponent } from './conta-recebimento-update.component';

describe('ContaRecebimento Management Update Component', () => {
  let comp: ContaRecebimentoUpdateComponent;
  let fixture: ComponentFixture<ContaRecebimentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contaRecebimentoFormService: ContaRecebimentoFormService;
  let contaRecebimentoService: ContaRecebimentoService;
  let pagadorService: PagadorService;
  let categoriaRecebimentoService: CategoriaRecebimentoService;
  let caixaService: CaixaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContaRecebimentoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContaRecebimentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContaRecebimentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contaRecebimentoFormService = TestBed.inject(ContaRecebimentoFormService);
    contaRecebimentoService = TestBed.inject(ContaRecebimentoService);
    pagadorService = TestBed.inject(PagadorService);
    categoriaRecebimentoService = TestBed.inject(CategoriaRecebimentoService);
    caixaService = TestBed.inject(CaixaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pagador query and add missing value', () => {
      const contaRecebimento: IContaRecebimento = { id: 456 };
      const pagador: IPagador = { id: 26530 };
      contaRecebimento.pagador = pagador;

      const pagadorCollection: IPagador[] = [{ id: 86811 }];
      jest.spyOn(pagadorService, 'query').mockReturnValue(of(new HttpResponse({ body: pagadorCollection })));
      const additionalPagadors = [pagador];
      const expectedCollection: IPagador[] = [...additionalPagadors, ...pagadorCollection];
      jest.spyOn(pagadorService, 'addPagadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      expect(pagadorService.query).toHaveBeenCalled();
      expect(pagadorService.addPagadorToCollectionIfMissing).toHaveBeenCalledWith(
        pagadorCollection,
        ...additionalPagadors.map(expect.objectContaining)
      );
      expect(comp.pagadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CategoriaRecebimento query and add missing value', () => {
      const contaRecebimento: IContaRecebimento = { id: 456 };
      const categoriaRecebimento: ICategoriaRecebimento = { id: 4488 };
      contaRecebimento.categoriaRecebimento = categoriaRecebimento;

      const categoriaRecebimentoCollection: ICategoriaRecebimento[] = [{ id: 81320 }];
      jest.spyOn(categoriaRecebimentoService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaRecebimentoCollection })));
      const additionalCategoriaRecebimentos = [categoriaRecebimento];
      const expectedCollection: ICategoriaRecebimento[] = [...additionalCategoriaRecebimentos, ...categoriaRecebimentoCollection];
      jest.spyOn(categoriaRecebimentoService, 'addCategoriaRecebimentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      expect(categoriaRecebimentoService.query).toHaveBeenCalled();
      expect(categoriaRecebimentoService.addCategoriaRecebimentoToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaRecebimentoCollection,
        ...additionalCategoriaRecebimentos.map(expect.objectContaining)
      );
      expect(comp.categoriaRecebimentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Caixa query and add missing value', () => {
      const contaRecebimento: IContaRecebimento = { id: 456 };
      const caixa: ICaixa = { id: 81798 };
      contaRecebimento.caixa = caixa;

      const caixaCollection: ICaixa[] = [{ id: 52287 }];
      jest.spyOn(caixaService, 'query').mockReturnValue(of(new HttpResponse({ body: caixaCollection })));
      const additionalCaixas = [caixa];
      const expectedCollection: ICaixa[] = [...additionalCaixas, ...caixaCollection];
      jest.spyOn(caixaService, 'addCaixaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      expect(caixaService.query).toHaveBeenCalled();
      expect(caixaService.addCaixaToCollectionIfMissing).toHaveBeenCalledWith(
        caixaCollection,
        ...additionalCaixas.map(expect.objectContaining)
      );
      expect(comp.caixasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contaRecebimento: IContaRecebimento = { id: 456 };
      const pagador: IPagador = { id: 61942 };
      contaRecebimento.pagador = pagador;
      const categoriaRecebimento: ICategoriaRecebimento = { id: 6610 };
      contaRecebimento.categoriaRecebimento = categoriaRecebimento;
      const caixa: ICaixa = { id: 72898 };
      contaRecebimento.caixa = caixa;

      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      expect(comp.pagadorsSharedCollection).toContain(pagador);
      expect(comp.categoriaRecebimentosSharedCollection).toContain(categoriaRecebimento);
      expect(comp.caixasSharedCollection).toContain(caixa);
      expect(comp.contaRecebimento).toEqual(contaRecebimento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaRecebimento>>();
      const contaRecebimento = { id: 123 };
      jest.spyOn(contaRecebimentoFormService, 'getContaRecebimento').mockReturnValue(contaRecebimento);
      jest.spyOn(contaRecebimentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaRecebimento }));
      saveSubject.complete();

      // THEN
      expect(contaRecebimentoFormService.getContaRecebimento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contaRecebimentoService.update).toHaveBeenCalledWith(expect.objectContaining(contaRecebimento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaRecebimento>>();
      const contaRecebimento = { id: 123 };
      jest.spyOn(contaRecebimentoFormService, 'getContaRecebimento').mockReturnValue({ id: null });
      jest.spyOn(contaRecebimentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaRecebimento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaRecebimento }));
      saveSubject.complete();

      // THEN
      expect(contaRecebimentoFormService.getContaRecebimento).toHaveBeenCalled();
      expect(contaRecebimentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaRecebimento>>();
      const contaRecebimento = { id: 123 };
      jest.spyOn(contaRecebimentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaRecebimento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contaRecebimentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePagador', () => {
      it('Should forward to pagadorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pagadorService, 'comparePagador');
        comp.comparePagador(entity, entity2);
        expect(pagadorService.comparePagador).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategoriaRecebimento', () => {
      it('Should forward to categoriaRecebimentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoriaRecebimentoService, 'compareCategoriaRecebimento');
        comp.compareCategoriaRecebimento(entity, entity2);
        expect(categoriaRecebimentoService.compareCategoriaRecebimento).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCaixa', () => {
      it('Should forward to caixaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(caixaService, 'compareCaixa');
        comp.compareCaixa(entity, entity2);
        expect(caixaService.compareCaixa).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
