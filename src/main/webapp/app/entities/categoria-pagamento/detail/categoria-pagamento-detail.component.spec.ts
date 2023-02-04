import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaPagamentoDetailComponent } from './categoria-pagamento-detail.component';

describe('CategoriaPagamento Management Detail Component', () => {
  let comp: CategoriaPagamentoDetailComponent;
  let fixture: ComponentFixture<CategoriaPagamentoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoriaPagamentoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ categoriaPagamento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CategoriaPagamentoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoriaPagamentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load categoriaPagamento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.categoriaPagamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
