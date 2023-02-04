import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContaPagamentoFormService } from './conta-pagamento-form.service';
import { ContaPagamentoService } from '../service/conta-pagamento.service';
import { IContaPagamento } from '../conta-pagamento.model';
import { IBeneficiario } from 'app/entities/beneficiario/beneficiario.model';
import { BeneficiarioService } from 'app/entities/beneficiario/service/beneficiario.service';
import { ICategoriaPagamento } from 'app/entities/categoria-pagamento/categoria-pagamento.model';
import { CategoriaPagamentoService } from 'app/entities/categoria-pagamento/service/categoria-pagamento.service';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { CaixaService } from 'app/entities/caixa/service/caixa.service';

import { ContaPagamentoUpdateComponent } from './conta-pagamento-update.component';

describe('ContaPagamento Management Update Component', () => {
  let comp: ContaPagamentoUpdateComponent;
  let fixture: ComponentFixture<ContaPagamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contaPagamentoFormService: ContaPagamentoFormService;
  let contaPagamentoService: ContaPagamentoService;
  let beneficiarioService: BeneficiarioService;
  let categoriaPagamentoService: CategoriaPagamentoService;
  let caixaService: CaixaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContaPagamentoUpdateComponent],
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
      .overrideTemplate(ContaPagamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContaPagamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contaPagamentoFormService = TestBed.inject(ContaPagamentoFormService);
    contaPagamentoService = TestBed.inject(ContaPagamentoService);
    beneficiarioService = TestBed.inject(BeneficiarioService);
    categoriaPagamentoService = TestBed.inject(CategoriaPagamentoService);
    caixaService = TestBed.inject(CaixaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Beneficiario query and add missing value', () => {
      const contaPagamento: IContaPagamento = { id: 456 };
      const beneficiario: IBeneficiario = { id: 19042 };
      contaPagamento.beneficiario = beneficiario;

      const beneficiarioCollection: IBeneficiario[] = [{ id: 69823 }];
      jest.spyOn(beneficiarioService, 'query').mockReturnValue(of(new HttpResponse({ body: beneficiarioCollection })));
      const additionalBeneficiarios = [beneficiario];
      const expectedCollection: IBeneficiario[] = [...additionalBeneficiarios, ...beneficiarioCollection];
      jest.spyOn(beneficiarioService, 'addBeneficiarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      expect(beneficiarioService.query).toHaveBeenCalled();
      expect(beneficiarioService.addBeneficiarioToCollectionIfMissing).toHaveBeenCalledWith(
        beneficiarioCollection,
        ...additionalBeneficiarios.map(expect.objectContaining)
      );
      expect(comp.beneficiariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CategoriaPagamento query and add missing value', () => {
      const contaPagamento: IContaPagamento = { id: 456 };
      const categoriaPagamento: ICategoriaPagamento = { id: 84782 };
      contaPagamento.categoriaPagamento = categoriaPagamento;

      const categoriaPagamentoCollection: ICategoriaPagamento[] = [{ id: 73550 }];
      jest.spyOn(categoriaPagamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaPagamentoCollection })));
      const additionalCategoriaPagamentos = [categoriaPagamento];
      const expectedCollection: ICategoriaPagamento[] = [...additionalCategoriaPagamentos, ...categoriaPagamentoCollection];
      jest.spyOn(categoriaPagamentoService, 'addCategoriaPagamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      expect(categoriaPagamentoService.query).toHaveBeenCalled();
      expect(categoriaPagamentoService.addCategoriaPagamentoToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaPagamentoCollection,
        ...additionalCategoriaPagamentos.map(expect.objectContaining)
      );
      expect(comp.categoriaPagamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Caixa query and add missing value', () => {
      const contaPagamento: IContaPagamento = { id: 456 };
      const caixa: ICaixa = { id: 84094 };
      contaPagamento.caixa = caixa;

      const caixaCollection: ICaixa[] = [{ id: 10191 }];
      jest.spyOn(caixaService, 'query').mockReturnValue(of(new HttpResponse({ body: caixaCollection })));
      const additionalCaixas = [caixa];
      const expectedCollection: ICaixa[] = [...additionalCaixas, ...caixaCollection];
      jest.spyOn(caixaService, 'addCaixaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      expect(caixaService.query).toHaveBeenCalled();
      expect(caixaService.addCaixaToCollectionIfMissing).toHaveBeenCalledWith(
        caixaCollection,
        ...additionalCaixas.map(expect.objectContaining)
      );
      expect(comp.caixasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contaPagamento: IContaPagamento = { id: 456 };
      const beneficiario: IBeneficiario = { id: 23218 };
      contaPagamento.beneficiario = beneficiario;
      const categoriaPagamento: ICategoriaPagamento = { id: 1573 };
      contaPagamento.categoriaPagamento = categoriaPagamento;
      const caixa: ICaixa = { id: 44598 };
      contaPagamento.caixa = caixa;

      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      expect(comp.beneficiariosSharedCollection).toContain(beneficiario);
      expect(comp.categoriaPagamentosSharedCollection).toContain(categoriaPagamento);
      expect(comp.caixasSharedCollection).toContain(caixa);
      expect(comp.contaPagamento).toEqual(contaPagamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaPagamento>>();
      const contaPagamento = { id: 123 };
      jest.spyOn(contaPagamentoFormService, 'getContaPagamento').mockReturnValue(contaPagamento);
      jest.spyOn(contaPagamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaPagamento }));
      saveSubject.complete();

      // THEN
      expect(contaPagamentoFormService.getContaPagamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contaPagamentoService.update).toHaveBeenCalledWith(expect.objectContaining(contaPagamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaPagamento>>();
      const contaPagamento = { id: 123 };
      jest.spyOn(contaPagamentoFormService, 'getContaPagamento').mockReturnValue({ id: null });
      jest.spyOn(contaPagamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaPagamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaPagamento }));
      saveSubject.complete();

      // THEN
      expect(contaPagamentoFormService.getContaPagamento).toHaveBeenCalled();
      expect(contaPagamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaPagamento>>();
      const contaPagamento = { id: 123 };
      jest.spyOn(contaPagamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaPagamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contaPagamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBeneficiario', () => {
      it('Should forward to beneficiarioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(beneficiarioService, 'compareBeneficiario');
        comp.compareBeneficiario(entity, entity2);
        expect(beneficiarioService.compareBeneficiario).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategoriaPagamento', () => {
      it('Should forward to categoriaPagamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoriaPagamentoService, 'compareCategoriaPagamento');
        comp.compareCategoriaPagamento(entity, entity2);
        expect(categoriaPagamentoService.compareCategoriaPagamento).toHaveBeenCalledWith(entity, entity2);
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
