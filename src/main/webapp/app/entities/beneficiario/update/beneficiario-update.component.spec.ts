import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeneficiarioFormService } from './beneficiario-form.service';
import { BeneficiarioService } from '../service/beneficiario.service';
import { IBeneficiario } from '../beneficiario.model';

import { BeneficiarioUpdateComponent } from './beneficiario-update.component';

describe('Beneficiario Management Update Component', () => {
  let comp: BeneficiarioUpdateComponent;
  let fixture: ComponentFixture<BeneficiarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beneficiarioFormService: BeneficiarioFormService;
  let beneficiarioService: BeneficiarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeneficiarioUpdateComponent],
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
      .overrideTemplate(BeneficiarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeneficiarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beneficiarioFormService = TestBed.inject(BeneficiarioFormService);
    beneficiarioService = TestBed.inject(BeneficiarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const beneficiario: IBeneficiario = { id: 456 };

      activatedRoute.data = of({ beneficiario });
      comp.ngOnInit();

      expect(comp.beneficiario).toEqual(beneficiario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiario>>();
      const beneficiario = { id: 123 };
      jest.spyOn(beneficiarioFormService, 'getBeneficiario').mockReturnValue(beneficiario);
      jest.spyOn(beneficiarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiario }));
      saveSubject.complete();

      // THEN
      expect(beneficiarioFormService.getBeneficiario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beneficiarioService.update).toHaveBeenCalledWith(expect.objectContaining(beneficiario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiario>>();
      const beneficiario = { id: 123 };
      jest.spyOn(beneficiarioFormService, 'getBeneficiario').mockReturnValue({ id: null });
      jest.spyOn(beneficiarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiario }));
      saveSubject.complete();

      // THEN
      expect(beneficiarioFormService.getBeneficiario).toHaveBeenCalled();
      expect(beneficiarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiario>>();
      const beneficiario = { id: 123 };
      jest.spyOn(beneficiarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beneficiarioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
