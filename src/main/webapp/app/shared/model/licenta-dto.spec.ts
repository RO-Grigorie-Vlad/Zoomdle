import { LicentaDTO } from './licenta-dto';

describe('LicentaDTO', () => {
  it('should create an instance', () => {
    expect(new LicentaDTO('acestUser', 1)).toBeTruthy();
  });
});
