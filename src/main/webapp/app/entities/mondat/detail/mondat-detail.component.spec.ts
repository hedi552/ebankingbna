import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MondatDetailComponent } from './mondat-detail.component';

describe('Mondat Management Detail Component', () => {
  let comp: MondatDetailComponent;
  let fixture: ComponentFixture<MondatDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MondatDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./mondat-detail.component').then(m => m.MondatDetailComponent),
              resolve: { mondat: () => of({ id: 21136 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MondatDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MondatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load mondat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MondatDetailComponent);

      // THEN
      expect(instance.mondat()).toEqual(expect.objectContaining({ id: 21136 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
