import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaRecebimentoFormService } from './categoria-recebimento-form.service';
import { CategoriaRecebimentoService } from '../service/categoria-recebimento.service';
import { ICategoriaRecebimento } from '../categoria-recebimento.model';

import { CategoriaRecebimentoUpdateComponent } from './categoria-recebimento-update.component';

describe('CategoriaRecebimento Management Update Component', () => {
  let comp: CategoriaRecebimentoUpdateComponent;
  let fixture: ComponentFixture<CategoriaRecebimentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaRecebimentoFormService: CategoriaRecebimentoFormService;
  let categoriaRecebimentoService: CategoriaRecebimentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaRecebimentoUpdateComponent],
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
      .overrideTemplate(CategoriaRecebimentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaRecebimentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaRecebimentoFormService = TestBed.inject(CategoriaRecebimentoFormService);
    categoriaRecebimentoService = TestBed.inject(CategoriaRecebimentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const categoriaRecebimento: ICategoriaRecebimento = { id: 456 };

      activatedRoute.data = of({ categoriaRecebimento });
      comp.ngOnInit();

      expect(comp.categoriaRecebimento).toEqual(categoriaRecebimento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaRecebimento>>();
      const categoriaRecebimento = { id: 123 };
      jest.spyOn(categoriaRecebimentoFormService, 'getCategoriaRecebimento').mockReturnValue(categoriaRecebimento);
      jest.spyOn(categoriaRecebimentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaRecebimento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaRecebimento }));
      saveSubject.complete();

      // THEN
      expect(categoriaRecebimentoFormService.getCategoriaRecebimento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaRecebimentoService.update).toHaveBeenCalledWith(expect.objectContaining(categoriaRecebimento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaRecebimento>>();
      const categoriaRecebimento = { id: 123 };
      jest.spyOn(categoriaRecebimentoFormService, 'getCategoriaRecebimento').mockReturnValue({ id: null });
      jest.spyOn(categoriaRecebimentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaRecebimento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaRecebimento }));
      saveSubject.complete();

      // THEN
      expect(categoriaRecebimentoFormService.getCategoriaRecebimento).toHaveBeenCalled();
      expect(categoriaRecebimentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategoriaRecebimento>>();
      const categoriaRecebimento = { id: 123 };
      jest.spyOn(categoriaRecebimentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaRecebimento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaRecebimentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
