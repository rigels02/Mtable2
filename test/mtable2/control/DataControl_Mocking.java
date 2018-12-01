package mtable2.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mtable2.io.FileIOImpl;
import mtable2.io.FilePersistenceManager;

import mtable2.model.MTable;
import mtable2.model.TableData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * mock DataControl calls to generate exceptions
 *
 * @author raitis
 */
//@RunWith(MockitoJUnitRunner.class)
public class DataControl_Mocking {

    public DataControl_Mocking() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    class MockFile_IO extends FileIOImpl<MTable> {

        public MockFile_IO() throws IOException, ClassNotFoundException {
            super();
        }

        @Override
        public Long delete(Long Id) throws Exception {
            //return super.delete(Id); //To change body of generated methods, choose Tools | Templates.
            throw new IOException("Delete ERROR!");
        }

        @Override
        public Long update(Long Id, MTable obj) throws IOException {
            throw new IOException("Write ERROR!");
        }

    };

    @Test
    public void createTable() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.control.DataControl_FileIOImpl_Mocking.createTable()");
        DataControl ctrlMock = mock(DataControl.class);
        //when(ctrlMock.create("Table_1", new MTable())).thenThrow(new Exception("Can not create!..."));
        doThrow(new Exception("Can not create!")).when(ctrlMock).create(anyString(), any(MTable.class));
        //when(ctrlMock.editItem(0, new TableData())).thenThrow(new Exception("Can not items!"));
        doThrow(new IOException("Can not edit!")).when(ctrlMock).editItem(anyInt(), any(TableData.class));
        try {
            ctrlMock.create("Table_1", new MTable());
        } catch (Exception e) {
            System.out.println("EXcepted: " + e.getMessage());
        }
        try {
            ctrlMock.editItem(0, new TableData());
        } catch (Exception e) {
            System.out.println("Excepted" + e.getMessage());
        }
        List myMockedList = mock(ArrayList.class);
        when(myMockedList.get(anyInt())).thenThrow(new NullPointerException());
        doThrow(new RuntimeException()).when(myMockedList).clear();

        try {
            System.out.println(myMockedList.get(1));
            myMockedList.clear();
        } catch (Exception e) {
            System.out.println("Exc: " + e.getMessage());
        }

        System.out.println("mtable2.control.DataControl_FileIOImpl_Mocking.createTable() - OK");
    }

    @Test
    public void createUpdateItemException() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.control.DataControl_FileIOImpl_Mocking.createUpdateItemException()");
        MockFile_IO io = new MockFile_IO();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        //All item edit methods use saveAllData-> io.update()
        try {
            ctrl.addItem(new TableData(new Date(), "Table_1Cat1", 12.0, "Table_1Note_1"));
        } catch (Exception ex) {
            System.out.println("Catched excpected: " + ex.getMessage());
        }
        try {
            ctrl.delete(ctrl.getSelectedTableId());
        } catch (Exception ex) {
            System.out.println("Catched excpected: " + ex.getMessage());
        }

        System.out.println("mtable2.control.DataControl_FileIOImpl_Mocking.createUpdateItemException() - OK");
    }

    @Test
    public void testCreateTableWithFileIO_Mock() throws Exception {
        System.out.println("mtable2.control.DataControl_Mocking.testCreateTableWithFileIO_Mock()");
        FileIOImpl ioMock = mock(FileIOImpl.class);
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(ioMock);
        DataControl ctrl = new DataControl(pm);
        doThrow(new Exception("Can not create!")).when(ioMock).create(anyString(), any(MTable.class));

        try {
            ctrl.create("Table_1", new MTable());
        } catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
        }
        System.out.println("mtable2.control.DataControl_Mocking.testCreateTableWithFileIO_Mock() - OK");
    }

}
