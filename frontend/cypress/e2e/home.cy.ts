beforeEach(() => {
  cy.visit('/');
});
it('barre de recherche visible', () => {
  cy.get('input[placeholder="Search..."]', { timeout: 10000}).should('be.visible');
});

it('navbar contient SignIn et Register quand déconnecté', () => {
  cy.contains('SignIn').should('be.visible');
  cy.contains('Register').should('be.visible');
  cy.contains('Logout').should('not.exist');
});

it('navigue vers register', () => {
  cy.get('a[href="/register"]').click();
  cy.url().should('include', '/register');
});
