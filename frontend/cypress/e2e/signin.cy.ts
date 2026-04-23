describe('SignIn Page', () => {
  beforeEach(() => {
   cy.visit('/')
  });
  it('navbar change après login admin', () => {
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('admin');
    cy.get('input[placeholder="Password"]').type('admin');
    cy.get('input[value="Login"]').click();
    cy.url().should('not.include', '/signin');
    // Vérifie que la navbar admin est bien présente
    cy.contains('Admin').should('be.visible');
    cy.contains('Review').should('be.visible');
    cy.contains('Logout').should('be.visible');
    // Vérifie que SignIn/Register ont disparu
    cy.contains('SignIn').should('not.exist');
    cy.contains('Register').should('not.exist');
  });

  it('navbar user normal après login', () => {
    // Crée d'abord un user lambda via register puis connecte-le
    cy.visit('/register');

    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Email"]').type('user@gmail.com');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Register"]').click();
    cy.visit('/signin');
    cy.get('input[placeholder="Username"]').type('usertest');
    cy.get('input[placeholder="Password"]').type('Password123!');
    cy.get('input[value="Login"]').click();
    // Un user normal ne voit pas Admin ni Review
    cy.contains('Admin').should('not.exist');
    cy.contains('Review').should('not.exist');
    cy.contains('Logout').should('be.visible');
  });
});
