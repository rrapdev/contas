import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContaRecebimentoDetailComponent } from './conta-recebimento-detail.component';

describe('ContaRecebimento Management Detail Component', () => {
  let comp: ContaRecebimentoDetailComponent;
  let fixture: ComponentFixture<ContaRecebimentoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContaRecebimentoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contaRecebimento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContaRecebimentoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContaRecebimentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contaRecebimento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contaRecebimento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
