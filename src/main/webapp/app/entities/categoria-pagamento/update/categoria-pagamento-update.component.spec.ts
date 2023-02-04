import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaPagamentoFormService } from './categoria-pagamento-form.service';
import { CategoriaPagamentoService } from '../service/categoria-pagamento.service';
import { ICategoriaPagamento } from '../categoria-pagamento.model';

import { CategoriaPagamentoUpdateComponent } from './categoria-pagamento-update.component';

describe('CategoriaPagamento Management Update Component', () => {
  let comp: CategoriaPagamentoUpdateComponent;
  let fixture: ComponentFixture<CategoriaPagamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaPagamentoFormService: CategoriaPagamentoFormService;
  let categoriaPagamentoService: CategoriaPagamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaPagamentoUpdateComponent],
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
      .overrideTemplate(CategoriaPagamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaPagamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaPagamentoFormService = TestBed.inject(CategoriaPagamentoFormService);
    categoriaPagamentoService = TestBed.inject(CategoriaPagamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const categoriaPagamento: ICategoriaPagamento = { id: 456 };

      activatedRoute.data = of({ categoriaPagamento });
      comp.ngOnInit();

      expect(comp.categoriaPagamento).toEqual(categoriaPagamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaPagamento>>();
      const categoriaPagamento = { id: 123 };
      jest.spyOn(categoriaPagamentoFormService, 'getCategoriaPagamento').mockReturnValue(categoriaPagamento);
      jest.spyOn(categoriaPagamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaPagamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaPagamento }));
      saveSubject.complete();

      // THEN
      expect(categoriaPagamentoFormService.getCategoriaPagamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaPagamentoService.update).toHaveBeenCalledWith(expect.objectContaining(categoriaPagamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaPagamento>>();
      const categoriaPagamento = { id: 123 };
      jest.spyOn(categoriaPagamentoFormService, 'getCategoriaPagamento').mockReturnValue({ id: null });
      jest.spyOn(categoriaPagamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaPagamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaPagamento }));
      saveSubject.complete();

      // THEN
      expect(categoriaPagamentoFormService.getCategoriaPagamento).toHaveBeenCalled();
      expect(categoriaPagamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaPagamento>>();
      const categoriaPagamento = { id: 123 };
      jest.spyOn(categoriaPagamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaPagamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaPagamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
