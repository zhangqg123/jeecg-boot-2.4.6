/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jeecg.modules.qwert.conn.qudong.exception;

/**
 * <p>QudongTransportException class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class QudongTransportException extends Exception {
    private static final long serialVersionUID = -1;

    private final int slaveId;

    /**
     * <p>Constructor for QudongTransportException.</p>
     */
    public QudongTransportException() {
        this.slaveId = -1;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param slaveId a int.
     */
    public QudongTransportException(int slaveId) {
        this.slaveId = slaveId;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param cause a {@link java.lang.Throwable} object.
     * @param slaveId a int.
     */
    public QudongTransportException(String message, Throwable cause, int slaveId) {
        super(message, cause);
        this.slaveId = slaveId;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param slaveId a int.
     */
    public QudongTransportException(String message, int slaveId) {
        super(message);
        this.slaveId = slaveId;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public QudongTransportException(String message) {
        super(message);
        this.slaveId = -1;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public QudongTransportException(Throwable cause) {
        super(cause);
        this.slaveId = -1;
    }

    /**
     * <p>Constructor for QudongTransportException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     * @param slaveId a int.
     */
    public QudongTransportException(Throwable cause, int slaveId) {
        super(cause);
        this.slaveId = slaveId;
    }

    /**
     * <p>Getter for the field <code>slaveId</code>.</p>
     *
     * @return a int.
     */
    public int getSlaveId() {
        return slaveId;
    }
}
