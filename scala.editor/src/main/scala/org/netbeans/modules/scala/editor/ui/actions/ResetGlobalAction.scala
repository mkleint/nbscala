/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2009 Sun Microsystems, Inc.
 */

package org.netbeans.modules.scala.editor.ui.actions

import java.awt.event.ActionEvent
import javax.swing.text.JTextComponent
import org.netbeans.modules.editor.NbEditorUtilities
import javax.swing.text.Document
import org.netbeans.editor.BaseAction
import org.openide.util.NbBundle
import org.openide.util.RequestProcessor
import org.netbeans.modules.scala.core.{ScalaGlobal}

/**
 *
 * @author Caoyuan Deng
 */
class ResetGlobalAction extends BaseAction(NbBundle.getMessage(classOf[ResetGlobalAction], "reset-scala-global"), 0
) with Runnable {

  var doc: Option[Document] = None

  override def isEnabled: Boolean = {
    true
  }

  def actionPerformed(evt: ActionEvent, comp: JTextComponent) {
    assert(comp ne null)
    doc = comp.getDocument match {
      case null => None
      case x => Some(x)
    }

    if (doc.isDefined) {
      RequestProcessor.getDefault.post(this)
    }
  }

  def run {
    val dob = NbEditorUtilities.getDataObject(doc.get)
    if (dob eq null) {
      return
    }

    val fo = dob.getPrimaryFile
    val global = ScalaGlobal.getGlobal(fo)
    if (global ne null) {
      ScalaGlobal.resetLate(global, ScalaGlobal.userRequest)
    }
  }
}

