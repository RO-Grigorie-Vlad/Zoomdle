import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { ConsultatieDetailComponent } from 'app/entities/consultatie/consultatie-detail.component';
import { Consultatie } from 'app/shared/model/consultatie.model';

describe('Component Tests', () => {
  describe('Consultatie Management Detail Component', () => {
    let comp: ConsultatieDetailComponent;
    let fixture: ComponentFixture<ConsultatieDetailComponent>;
    const route = ({ data: of({ consultatie: new Consultatie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [ConsultatieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConsultatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsultatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load consultatie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consultatie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
