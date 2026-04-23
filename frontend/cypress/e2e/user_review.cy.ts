describe('New Page', () => {
  beforeEach(() => {
    // La page /new nécessite d'être connecté
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Login"]').click();
    cy.url().should('include', '/');
    cy.contains('Logout').should('be.visible');
    cy.contains('New').click();
  });

it('should send article to review queue', () => {
     cy.get('input[name="gameName"]').type('Subway');
     cy.get('input[name="articleName"]').type('Subway');
     cy.get('.ProseMirror').click().type('Très bon jeu');
     cy.get('input[value=Create]').click();
  });
  it('should not create article with empty fields', () => {
    cy.get('input[value=Create').click();
    cy.url().should('include', '/new');
  });
});

