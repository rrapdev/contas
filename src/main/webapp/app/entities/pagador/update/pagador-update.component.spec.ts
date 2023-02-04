import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PagadorFormService } from './pagador-form.service';
import { PagadorService } from '../service/pagador.service';
import { IPagador } from '../pagador.model';

import { PagadorUpdateComponent } from './pagador-update.component';

describe('Pagador Management Update Component', () => {
  let comp: PagadorUpdateComponent;
  let fixture: ComponentFixture<PagadorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pagadorFormService: PagadorFormService;
  let pagadorService: PagadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PagadorUpdateComponent],
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
      .overrideTemplate(PagadorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PagadorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pagadorFormService = TestBed.inject(PagadorFormService);
    pagadorService = TestBed.inject(PagadorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pagador: IPagador = { id: 456 };

      activatedRoute.data = of({ pagador });
      comp.ngOnInit();

      expect(comp.pagador).toEqual(pagador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagador>>();
      const pagador = { id: 123 };
      jest.spyOn(pagadorFormService, 'getPagador').mockReturnValue(pagador);
      jest.spyOn(pagadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pagador }));
      saveSubject.complete();

      // THEN
      expect(pagadorFormService.getPagador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pagadorService.update).toHaveBeenCalledWith(expect.objectContaining(pagador));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagador>>();
      const pagador = { id: 123 };
      jest.spyOn(pagadorFormService, 'getPagador').mockReturnValue({ id: null });
      jest.spyOn(pagadorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pagador }));
      saveSubject.complete();

      // THEN
      expect(pagadorFormService.getPagador).toHaveBeenCalled();
      expect(pagadorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPagador>>();
      const pagador = { id: 123 };
      jest.spyOn(pagadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pagador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pagadorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
