-- ============================================================
-- Rwanda ERP - Database Routines (PostgreSQL)
-- Task 5: Function + Trigger for payslip messaging
-- Run these in psql or pgAdmin after the app creates the tables
-- ============================================================


-- -------------------------------------------------------
-- FUNCTION: approve_payroll(month, year)
-- Approves all PENDING payslips for a given month/year,
-- updates status to PAID (trigger then fires per row).
-- -------------------------------------------------------
CREATE OR REPLACE FUNCTION approve_payroll(p_month INT, p_year INT)
RETURNS VOID AS $$
BEGIN
    UPDATE payslips
    SET status = 'PAID'
    WHERE month = p_month
      AND year  = p_year
      AND status = 'PENDING';
END;
$$ LANGUAGE plpgsql;


-- -------------------------------------------------------
-- TRIGGER FUNCTION: fn_after_payslip_paid()
-- Fires AFTER UPDATE on payslips (row-level).
-- When status changes to PAID, inserts a notification.
-- -------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_after_payslip_paid()
RETURNS TRIGGER AS $$
DECLARE
    v_first_name  VARCHAR(100);
    v_institution VARCHAR(200);
    v_emp_code    VARCHAR(50);
    v_message     VARCHAR(1000);
BEGIN
    -- Only act when status transitions to PAID
    IF NEW.status = 'PAID' AND OLD.status <> 'PAID' THEN

        -- Fetch employee first name
        SELECT first_name INTO v_first_name
        FROM employees
        WHERE id = NEW.employee_id;

        -- Fetch institution and employee code
        SELECT institution, employee_code
        INTO v_institution, v_emp_code
        FROM employments
        WHERE employee_id = NEW.employee_id;

        -- Build the notification message
        v_message := FORMAT(
            'Dear %s Your salary of %s/%s from %s %s FRW has been credited to your %s account Successfully.',
            v_first_name,
            LPAD(NEW.month::TEXT, 2, '0'),
            NEW.year::TEXT,
            v_institution,
            TO_CHAR(NEW.net_salary, 'FM999,999,990.00'),
            v_emp_code
        );

        -- Insert into messages table
        INSERT INTO messages (employee_id, message, month, year, sent_at)
        VALUES (NEW.employee_id, v_message, NEW.month, NEW.year, NOW());

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- -------------------------------------------------------
-- TRIGGER: trg_after_payslip_paid
-- Attach the function to the payslips table
-- -------------------------------------------------------
DROP TRIGGER IF EXISTS trg_after_payslip_paid ON payslips;

CREATE TRIGGER trg_after_payslip_paid
AFTER UPDATE ON payslips
FOR EACH ROW
EXECUTE FUNCTION fn_after_payslip_paid();


-- -------------------------------------------------------
-- USAGE EXAMPLE:
--   SELECT approve_payroll(6, 2025);
--
-- This updates all June 2025 PENDING payslips to PAID.
-- The trigger fires per row and inserts a message like:
-- "Dear Mugabo Your salary of 06/2025 from RCA 57,800.00 FRW
--  has been credited to your EMP-0001 account Successfully."
-- -------------------------------------------------------
